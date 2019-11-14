package SevenWonders.GameLogic;

import SevenWonders.GameLogic.Enums.HERO_EFFECT_TYPE;

public class Hero {
    private String name;
    private HERO_EFFECT_TYPE heroEffect;

    public Hero(String name, HERO_EFFECT_TYPE effect)
    {
        this.name = name;
        this.heroEffect = effect;
    }

    public String getName() { return name; }

    public HERO_EFFECT_TYPE getHeroEffect() {
        return heroEffect;
    }

}