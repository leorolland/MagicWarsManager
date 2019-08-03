package net.magicwars.manager.kits.classes;

import net.magicwars.manager.kits.Classe;
import net.magicwars.manager.kits.Kit;
import net.magicwars.manager.kits.kits.SorceleurKit1;
import net.magicwars.manager.kits.kits.SorceleurKit2;

public class SorceleurClasse extends Classe {

	@Override
	public String getName() {
		return "Sorceleur";
	}

	@Override
	public String getDescription() {
		return "Un soldat avec des sorts plus faibles, mais qui sait manier son epée !";
	}

	@Override
	public Kit getKit(Integer lvl) {
		if (lvl<=1)
			return new SorceleurKit1();
		else
			return new SorceleurKit2();
	}

}
