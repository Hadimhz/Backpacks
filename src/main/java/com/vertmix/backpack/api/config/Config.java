package com.vertmix.backpack.api.config;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;

import java.util.Map;

public class Config {

    public int maxLevel = 100;
    public int increaseSize = 1000;
    public int increasePrice = 1000;
    public Map<Material, Double> prices = ImmutableMap.of(
            Material.STONE, 1.0,
            Material.IRON_BLOCK, 5.0
    );

}
