package me.leonidussaks.voteForCommand.menu

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack


abstract class MenuBase(
    size: Int = 27,
    title: Component = Component.text("", NamedTextColor.BLACK)
)
    : Listener, InventoryHolder{
    private val _inventory: Inventory = Bukkit.createInventory(this, size, title)
    override fun getInventory(): Inventory = _inventory

    open fun open(player: Player) {
        player.openInventory(_inventory)
    }

    fun makeItem(material: Material, name: Component, lore: List<Component> = emptyList<Component>()): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta ?: return item

        meta.displayName(name)
        if (lore.isNotEmpty()) meta.lore(lore)

        item.itemMeta = meta
        return item
    }

    // listeners
    open fun onClick(e: InventoryClickEvent) {
        val clicked = e.currentItem ?: return
        if (clicked.type.isAir) return
    }

}