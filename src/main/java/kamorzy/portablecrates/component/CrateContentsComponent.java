package kamorzy.portablecrates.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import kamorzy.portablecrates.blockentity.CrateBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record CrateContentsComponent(List<CrateContentsComponent.SlotEntry> entries) {
    public static final Codec<CrateContentsComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    SlotEntry.CODEC.listOf()
                            .fieldOf("entries")
                            .forGetter(CrateContentsComponent::entries)
            ).apply(instance, CrateContentsComponent::new)
    );

    public record SlotEntry(int slot, ItemStack stack) {
        public static final Codec<SlotEntry> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.intRange(0, CrateBlockEntity.SLOT_COUNT - 1)
                                .fieldOf("slot")
                                .forGetter(SlotEntry::slot),
                        ItemStack.CODEC
                                .fieldOf("stack")
                                .forGetter(SlotEntry::stack)
                ).apply(instance, SlotEntry::new)
        );

        public SlotEntry {
            if (stack.isEmpty()) {
                throw new IllegalArgumentException("CrateContentsComponent cannot store empty item stacks");
            }

            stack = stack.copy();
        }
    }

    public CrateContentsComponent {
        Map<Integer, SlotEntry> normalized = new LinkedHashMap<>();

        for (SlotEntry entry : entries) {
            if (entry == null) {
                continue;
            }

            int slot = entry.slot();
            ItemStack stack = entry.stack();

            if (slot < 0 || slot >= CrateBlockEntity.SLOT_COUNT) {
                continue;
            }

            if (stack.isEmpty()) {
                continue;
            }

            normalized.put(slot, new SlotEntry(slot, stack.copy()));
        }

        List<SlotEntry> sortedEntries = new ArrayList<>(normalized.values());
        sortedEntries.sort(Comparator.comparingInt(SlotEntry::slot));

        entries = List.copyOf(sortedEntries);
    }

    public static @NonNull CrateContentsComponent fromItems(List<ItemStack> items) {
        List<SlotEntry> entries = new ArrayList<>();

        for (int i = 0; i < Math.min(items.size(), CrateBlockEntity.SLOT_COUNT); i++) {
            ItemStack stack = items.get(i);

            if (!stack.isEmpty()) {
                entries.add(new SlotEntry(i, stack.copy()));
            }
        }

        return new CrateContentsComponent(entries);
    }

    public @NonNull NonNullList<ItemStack> toNonNullList() {
        NonNullList<ItemStack> result = NonNullList.withSize(CrateBlockEntity.SLOT_COUNT, ItemStack.EMPTY);

        for (SlotEntry entry : this.entries) {
            result.set(entry.slot(), entry.stack().copy());
        }

        return result;
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }
}