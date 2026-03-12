package com.happiness.item;

import com.happiness.HappinessManager;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

/**
 * Happiness Pill - can be consumed by player OR given to villagers
 * 
 * Player consumption: +25 happiness instantly
 * Villager gift: They give you diamonds in gratitude
 */
public class HappinessPillItem extends Item {

    // Food component makes it consumable
    public static final FoodComponent HAPPINESS_FOOD = new FoodComponent.Builder()
            .nutrition(0)           // No hunger restoration
            .saturationModifier(0)  // No saturation
            .alwaysEdible()         // Can eat even when full
            .build();

    public HappinessPillItem(Settings settings) {
        super(settings.food(HAPPINESS_FOOD));
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity player && !world.isClient()) {
            // Player consumed the pill - boost happiness!
            HappinessManager.getInstance().onPillConsumed(player);
            
            ServerWorld serverWorld = (ServerWorld) world;
            
            // Play happy sound
            serverWorld.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.5f, 1.5f);
            
            // Spawn particles around player
            for (int i = 0; i < 15; i++) {
                double offsetX = (serverWorld.random.nextDouble() - 0.5) * 2;
                double offsetY = serverWorld.random.nextDouble() * 2;
                double offsetZ = (serverWorld.random.nextDouble() - 0.5) * 2;
                serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                        player.getX() + offsetX,
                        player.getY() + offsetY,
                        player.getZ() + offsetZ,
                        1, 0, 0, 0, 0);
            }
            
            // Show message
            float happiness = HappinessManager.getInstance().getHappiness(player);
            String mood = HappinessManager.getInstance().getMoodDescription(happiness);
            player.sendMessage(Text.literal("You feel a wave of happiness wash over you! (" + mood + ")")
                    .formatted(Formatting.YELLOW), true);
        }
        
        return super.finishUsing(stack, world, user);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (entity instanceof VillagerEntity villager) {
            if (!player.getWorld().isClient()) {
                ServerWorld serverWorld = (ServerWorld) player.getWorld();
                
                // Consume one pill
                stack.decrement(1);
                
                // Give the player diamonds (1-3 random)
                int diamondCount = 1 + serverWorld.random.nextInt(3);
                ItemStack diamonds = new ItemStack(Items.DIAMOND, diamondCount);
                
                if (!player.getInventory().insertStack(diamonds)) {
                    player.dropItem(diamonds, false);
                }
                
                // Play happy sounds
                serverWorld.playSound(null, villager.getX(), villager.getY(), villager.getZ(),
                        SoundEvents.ENTITY_VILLAGER_YES, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                serverWorld.playSound(null, villager.getX(), villager.getY(), villager.getZ(),
                        SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.NEUTRAL, 0.5f, 1.5f);
                
                // Spawn heart particles around the villager
                for (int i = 0; i < 10; i++) {
                    double offsetX = (serverWorld.random.nextDouble() - 0.5) * 1.5;
                    double offsetY = serverWorld.random.nextDouble() * 1.5 + 0.5;
                    double offsetZ = (serverWorld.random.nextDouble() - 0.5) * 1.5;
                    serverWorld.spawnParticles(ParticleTypes.HEART,
                            villager.getX() + offsetX,
                            villager.getY() + offsetY,
                            villager.getZ() + offsetZ,
                            1, 0, 0, 0, 0);
                }
                
                // Send message to player
                player.sendMessage(Text.literal("The villager is overjoyed! They gave you " + diamondCount + " diamond" + (diamondCount > 1 ? "s" : "") + "!")
                        .formatted(Formatting.AQUA), true);
            }
            
            return ActionResult.SUCCESS;
        }
        
        return ActionResult.PASS;
    }
}
