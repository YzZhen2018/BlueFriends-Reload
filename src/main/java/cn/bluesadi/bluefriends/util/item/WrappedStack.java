package cn.bluesadi.bluefriends.util.item;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WrappedStack {
    public ItemStack base;
    public NbtCompound nbt;

    public WrappedStack(ItemStack base){
        this.base = MinecraftReflection.getBukkitItemStack(base);
        try{
            this.nbt = ((NbtCompound)NbtFactory.fromItemTag(this.base));
        }
        catch (Exception e){
            if (!base.hasItemMeta()){
                this.nbt = null;
            }
            else{
                ItemMeta meta = base.getItemMeta();
                if (meta == null) {
                    meta = Bukkit.getServer().getItemFactory().getItemMeta(base.getType());
                }
                base.setItemMeta(meta);
                this.base = MinecraftReflection.getBukkitItemStack(base);
                this.nbt = ((NbtCompound)NbtFactory.fromItemTag(this.base));
            }
        }
    }

    public int getAmount()
    {
        return this.base.getAmount();
    }

    public int getDamage()
    {
        return this.base.getDurability();
    }

    public WrappedStack setAmount(int amt)
    {
        this.base.setAmount(amt);
        return this;
    }

    public WrappedStack setDamage(int dmg)
    {
        this.base.setDurability((short)dmg);
        return this;
    }

    public Material getMaterial()
    {
        return this.base.getType();
    }

    public int getMaterialId()
    {
        return this.base.getType().getId();
    }

    public WrappedStack clone()
    {
        WrappedStack clone = new WrappedStack(new ItemStack(this.base.getType(), this.base.getAmount(), this.base.getDurability()));
        if (this.nbt != null) {
            clone.setNbt((NbtCompound)this.nbt.deepClone());
        }
        return clone;
    }

    public WrappedStack setNbt(NbtCompound comp)
    {
        NbtFactory.setItemTag(this.base, comp);
        this.nbt = comp;
        return this;
    }

    public NbtCompound getNbt()
    {
        return this.nbt;
    }

    public boolean hasDisplayName()
    {
        return getDisplayTag().containsKey("Name");
    }

    public String getDisplayName()
    {
        return getDisplayTag().getString("Name");
    }

    public WrappedStack setDisplayName(String name)
    {
        NbtCompound disp = getDisplayTag();
        disp.remove("Name");
        disp.put("Name", name);
        return this;
    }

    public boolean hasLore()
    {
        return getDisplayTag().containsKey("Lore");
    }

    public List<String> getLore()
    {
        List<String> lore = new ArrayList();
        for (Object ob : this.nbt.getCompound("display").getList("Lore")) {
            lore.add(ob.toString());
        }
        return lore;
    }

    public WrappedStack setLore(List<String> lore)
    {
        NbtCompound disp = getDisplayTag();
        disp.remove("Lore");
        disp.put(NbtFactory.ofList("Lore", lore));
        return this;
    }

    public WrappedStack addLoreLast(List<String> lore)
    {
        if (hasLore())
        {
            List<String> old = getLore();
            old.addAll(lore);
            setLore(old);
        }
        else
        {
            setLore(lore);
        }
        return this;
    }

    public WrappedStack addLoreBefore(List<String> lore)
    {
        if (hasLore())
        {
            lore.addAll(getLore());
            setLore(lore);
        }
        else
        {
            setLore(lore);
        }
        return this;
    }

    public WrappedStack makeGlow()
    {
        if (!this.nbt.containsKey("ench")) {
            this.nbt.put(NbtFactory.ofList("ench", new Object[0]));
        }
        return this;
    }

    public WrappedStack clearGlow()
    {
        this.nbt.remove("ench");
        return this;
    }

    public WrappedStack tag()
    {
        this.nbt.put(NbtFactory.of("market", 1));
        addLoreLast(Arrays.asList(new String[] { "Has tag" }));
        return this;
    }

    public boolean hasTag()
    {
        return this.nbt.containsKey("market");
    }

    public NbtCompound getDisplayTag()
    {
        if (!this.nbt.containsKey("display")) {
            this.nbt.put(NbtFactory.ofCompound("display"));
        }
        return this.nbt.getCompound("display");
    }

    public ItemStack bukkit()
    {
        return this.base;
    }

    public WrappedStack checkNbt()
    {
        if ((this.nbt != null) && (!this.nbt.iterator().hasNext())) {
            setNbt(null);
        }
        return this;
    }

    public boolean equals(Object ob)
    {
        if (ob == this) {
            return true;
        }
        if ((ob instanceof WrappedStack))
        {
            WrappedStack stack = (WrappedStack)ob;
            if ((stack.getMaterial().equals(getMaterial())) && (stack.getDamage() == getDamage()))
            {
                if (stack.nbt != null) {
                    return stack.nbt.equals(this.nbt);
                }
                return true;
            }
        }
        return false;
    }
}
