package com.Polarice3.Goety.init;

import com.Polarice3.Goety.Goety;
import com.Polarice3.Goety.common.ritual.CraftItemRitual;
import com.Polarice3.Goety.common.ritual.SummonRitual;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

public class ModRituals {

    public static final IForgeRegistry<ModRitualFactory> RITUAL_FACTORIES = RegistryManager.ACTIVE.getRegistry(ModRitualFactory.class);
    public static final DeferredRegister<ModRitualFactory> RITUALS = DeferredRegister.create(RITUAL_FACTORIES, Goety.MOD_ID);

    public static final RegistryObject<ModRitualFactory> CRAFT_RITUAL =
            RITUALS.register("craft",
                    () -> new ModRitualFactory(CraftItemRitual::new));

    public static final RegistryObject<ModRitualFactory> SUMMON_RITUAL =
            RITUALS.register("summon",
                    () -> new ModRitualFactory((ritual) -> new SummonRitual(ritual, false)));

    public static final RegistryObject<ModRitualFactory> SUMMON_TAMED_RITUAL =
            RITUALS.register("summon_tamed",
                    () -> new ModRitualFactory((ritual) -> new SummonRitual(ritual, false)));

}
/*
 * MIT License
 *
 * Copyright 2020 klikli-dev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT
 * OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */