package net.punchtree.minigames.region;

import net.punchtree.minigames.MinigamesPlugin;
import net.punchtree.util.color.PunchTreeColor;
import net.punchtree.util.debugvar.DebugVars;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RegionVisualizer {

    public static void drawArea(Area area, PunchTreeColor color) {
        if (area instanceof Region) {
            drawRegion((Region) area, color);
        } else if (area instanceof MultiRegion) {
            drawMultiRegion((MultiRegion) area, color);
        }
    }

    private static void drawMultiRegion(MultiRegion multiRegion, PunchTreeColor color) {
        multiRegion.getComponentAreas().forEach(area -> drawArea(area, color));
    }

    private static void drawRegion(Region region, PunchTreeColor color) {
        Location min = region.getMin();
        Location max = region.getMax();
        Location minX = min.clone();
        minX.setX(max.getX());
        Location minY = min.clone();
        minY.setY(max.getY());
        Location minZ = min.clone();
        minZ.setZ(max.getZ());
        Location maxX = max.clone();
        maxX.setX(min.getX());
        Location maxY = max.clone();
        maxY.setY(min.getY());
        Location maxZ = max.clone();
        maxZ.setZ(min.getZ());

        new BukkitRunnable() {
            int i = 0;
            public void run() {
                if (i > DebugVars.getDecimalAsFloat("region_visualizer_duration_seconds", 5) * 20) {
                    this.cancel();
                    return;
                }

                //cube lines
                drawLine(min, minX, color);
                drawLine(min, minY, color);
                drawLine(min, minZ, color);
                drawLine(max, maxX, color);
                drawLine(max, maxY, color);
                drawLine(max, maxZ, color);

                drawLine(minX, maxZ, color);
                drawLine(minY, maxZ, color);
                drawLine(minY, maxX, color);
                drawLine(minZ, maxX, color);

                drawLine(minX, maxY, color);
                drawLine(minZ, maxY, color);

                // diag lines
                drawLine(min, maxX, color);
                drawLine(min, maxY, color);
                drawLine(min, maxZ, color);
                drawLine(max, minX, color);
                drawLine(max, minY, color);
                drawLine(max, minZ, color);

                ++i;
            }
        }.runTaskTimer(MinigamesPlugin.getInstance(), 0, 1);
    }

    private static void drawLine(Location a, Location b, PunchTreeColor color) {
        float STEPS_PER_BLOCK = DebugVars.getDecimalAsFloat("region_visualizer_steps_per_block", 3);
        double length = a.distance(b);
        spawnParticleLine(a, b, (int) (length * STEPS_PER_BLOCK), color);
    }

    private static void spawnParticleLine(Location a, Location b, int steps, PunchTreeColor color) {
        Vector difference = b.clone().toVector().subtract(a.toVector());
        difference.multiply(1d/(steps-1));
        // change <= vs < for endpoint
        for ( int i = 0; i < steps; ++i ) {
            Location l = a.clone().add(difference.clone().multiply(i));
            spawnParticle(l, color);
        }
    }

    private static void spawnParticle(Location location, PunchTreeColor color) {
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, new Particle.DustOptions(color.getBukkitColor(), 1));
    }
}
