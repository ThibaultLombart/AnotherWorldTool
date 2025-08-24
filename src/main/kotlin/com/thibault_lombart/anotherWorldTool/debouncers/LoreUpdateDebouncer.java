package com.thibault_lombart.anotherWorldTool.debouncers;

public final class LoreUpdateDebouncer {

    private final java.util.Map<java.util.UUID, org.bukkit.scheduler.BukkitTask> pending = new java.util.HashMap<>();
    private final org.bukkit.plugin.Plugin plugin;

    public LoreUpdateDebouncer(org.bukkit.plugin.Plugin plugin) { this.plugin = plugin; }

    public void schedule(java.util.UUID uuid, Runnable task, long delayTicks) {
        org.bukkit.scheduler.BukkitTask previous = pending.remove(uuid);
        if (previous != null) previous.cancel();
        org.bukkit.scheduler.BukkitTask newTask =
                org.bukkit.Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    pending.remove(uuid);
                    task.run();
                }, delayTicks);
        pending.put(uuid, newTask);
    }

    public void cancel(java.util.UUID uuid) {
        org.bukkit.scheduler.BukkitTask t = pending.remove(uuid);
        if (t != null) t.cancel();
    }

}
