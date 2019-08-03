package net.magicwars.manager.kits.classes;

import net.magicwars.manager.kits.Classe;
import net.magicwars.manager.kits.Kit;
import net.magicwars.manager.kits.kits.MageblancKit5;
import net.magicwars.manager.kits.kits.SorceleurKit1;
import net.magicwars.manager.kits.kits.SorceleurKit2;

public class MageblancClasse extends Classe {

	@Override
	public String getName() {
		return "Mage Blanc";
	}

	@Override
	public String getDescription() {
		return "Un puissant magicien qui utilise les forces de la lumière pour faire régner la paix.";
	}

	@Override
	public Kit getKit(Integer lvl) {
		return new MageblancKit5();
	}

}
