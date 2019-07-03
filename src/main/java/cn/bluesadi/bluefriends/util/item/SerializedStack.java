package cn.bluesadi.bluefriends.util.item;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.io.NbtBinarySerializer;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

class SerializedStack {

    private long id;
    private String mat;
    private int damage = 0;
    private String nbt;
    private int amount;

    SerializedStack() {}

    SerializedStack(WrappedStack stack)
           throws IOException
   {
       this.mat = stack.getMaterial().toString();
       this.damage = stack.getDamage();
       this.amount = stack.getAmount();
       if ((stack.nbt != null) && (stack.nbt.iterator().hasNext()))
       {
           DataOutputStream output = null;
           ByteArrayOutputStream stream = new ByteArrayOutputStream();
           NbtBinarySerializer.DEFAULT.serialize(stack.nbt, output = new DataOutputStream(new GZIPOutputStream(stream)));
           if (output != null) {
               output.close();
           }
           this.nbt = Base64.encodeBase64String(stream.toByteArray());
           stream.close();
       }
   }

    long getId()
   {
       return this.id;
   }

    WrappedStack buildStack()
           throws IOException
   {
       if ((this.mat == null) || (Material.getMaterial(this.mat) == null)) {
           throw new IOException("Material not found!");
       }
       ItemStack base = new ItemStack(Material.getMaterial(this.mat), amount, (short)this.damage);
       WrappedStack stack = new WrappedStack(base);
       if (this.nbt != null)
       {
           ByteArrayInputStream stream = new ByteArrayInputStream(Base64.decodeBase64(this.nbt));
           DataInputStream input = null;
           NbtCompound comp = NbtBinarySerializer.DEFAULT.deserializeCompound(input = new DataInputStream(new GZIPInputStream(stream)));
           input.close();
           stream.close();
           stack.setNbt(comp);
       }
       return stack;
   }

   public int hashCode()
   {
       return this.mat.hashCode() + (this.nbt == null ? 0 : this.nbt.hashCode()) + this.damage;
   }

   public boolean equals(Object ob)
   {
       if (ob == this) {
           return true;
       }
       if ((ob instanceof SerializedStack))
       {
           SerializedStack st = (SerializedStack)ob;
           if ((st.mat.equals(this.mat)) && (st.damage == this.damage) && (st.nbt.equals(this.nbt))) {
               return true;
           }
       }
       return false;
   }
}
