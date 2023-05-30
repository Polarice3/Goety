package com.Polarice3.Goety.client.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public class ShockwaveParticleOption implements IParticleData {
   public static final Codec<ShockwaveParticleOption> CODEC = RecordCodecBuilder.create((p_235952_) -> {
      return p_235952_.group(Codec.INT.fieldOf("delay").forGetter((p_235954_) -> {
         return p_235954_.delay;
      })).apply(p_235952_, ShockwaveParticleOption::new);
   });
   public static final IDeserializer<ShockwaveParticleOption> DESERIALIZER = new IDeserializer<ShockwaveParticleOption>() {
      public ShockwaveParticleOption fromCommand(ParticleType<ShockwaveParticleOption> p_235961_, StringReader p_235962_) throws CommandSyntaxException {
         p_235962_.expect(' ');
         int i = p_235962_.readInt();
         return new ShockwaveParticleOption(i);
      }

      public ShockwaveParticleOption fromNetwork(ParticleType<ShockwaveParticleOption> p_235964_, PacketBuffer p_235965_) {
         return new ShockwaveParticleOption(p_235965_.readVarInt());
      }
   };
   private final int delay;

   public ShockwaveParticleOption(int p_235949_) {
      this.delay = p_235949_;
   }

   public void writeToNetwork(PacketBuffer p_235956_) {
      p_235956_.writeVarInt(this.delay);
   }

   public String writeToString() {
      return String.format(Locale.ROOT, "%s %d", Registry.PARTICLE_TYPE.getKey(this.getType()), this.delay);
   }

   public ParticleType<ShockwaveParticleOption> getType() {
      return ModParticleTypes.SHOCKWAVE.get();
   }

   public int getDelay() {
      return this.delay;
   }
}