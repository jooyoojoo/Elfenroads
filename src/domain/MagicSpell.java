package domain;

import enums.MagicSpellType;
import enums.CounterUnitType;
import windows.MainFrame;

public class MagicSpell extends CounterUnit{
	
	
	public MagicSpell(MagicSpellType pType, int resizeWidth, int resizeHeight) {
		super(pType, resizeWidth, resizeHeight, pType.toString());//Magic spell pictures are renamed as M0+MagicSpellType.
		super.initializeMouseListener();
	}
	
	@Override
    public CounterUnit getNew() {
    	return new MagicSpell((MagicSpellType)this.getType(), MainFrame.instance.getWidth() * 67 / 1440, MainFrame.instance.getHeight() * 60 / 900);
    }
	
}
