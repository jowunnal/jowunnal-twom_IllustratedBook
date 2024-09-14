package com.jinproject.domain.model

enum class Stat(val displayName: String, val displayOtherLanguage: String, val range: IntRange? = null) {
    HP(displayName = "체력", displayOtherLanguage = "Hp"),
    MP(displayName = "마나", displayOtherLanguage = "Mp", range = 0..40),
    HPPER(displayName = "체력%", displayOtherLanguage = "Hp%"),
    MPPER(displayName = "마나%", displayOtherLanguage = "Mp%"),
    HPREGEN(displayName = "체력재생", displayOtherLanguage = "HpRegen"),
    MPREGEN(displayName = "마나재생", displayOtherLanguage = "MpRegen", range = 0..4),
    HR(displayName = "명중", displayOtherLanguage = "Hr", range = 0..12),
    CRI(displayName = "크리티컬", displayOtherLanguage = "Cri", range = 0..7),
    STATINT(displayName = "지능", displayOtherLanguage = "Int", range = 0..14),
    STATSTR(displayName = "힘", displayOtherLanguage = "Str", range = 0..14),
    STATDEX(displayName = "민첩", displayOtherLanguage = "Dex", range = 0..14),
    MOVE(displayName = "이동속도", displayOtherLanguage = "Move", range = 0..5),
    ARMOR(displayName = "방어력", displayOtherLanguage = "Armor"),
    PVEDMG(displayName = "pve데미지증가", displayOtherLanguage = "PveDmgUp"),
    PVPDMG(displayName = "pvp데미지증가", displayOtherLanguage = "PvpDmgUp"),
    PVEDMGPER(displayName = "pve데미지증가%", displayOtherLanguage = "PveDmgUp%"),
    PVPDMGPER(displayName = "pvp데미지증가%", displayOtherLanguage = "PvpDmgUp%"),
    PVEDMGDOWN(displayName = "pve데미지감소", displayOtherLanguage = "PveDmgDown"),
    PVPDMGDOWN(displayName = "pvp데미지감소", displayOtherLanguage = "PvpDmgDown"),
    PVEDMGDOWNPER(displayName = "pve데미지감소%", displayOtherLanguage = "PveDmgDown%"),
    PVPDMGDOWNPER(displayName = "pvp데미지감소%", displayOtherLanguage = "PvpDmgDown%"),
    GOLDDROP(displayName = "골드드랍률", displayOtherLanguage = "GoldDrop"),
    ITEMDROP(displayName = "아이템드랍률", displayOtherLanguage = "ItemDrop"),
    BOSSDMGPER(displayName = "보스데미지%", displayOtherLanguage = "BossDmg%"),
    CRITDMGDOWN(displayName = "크리티컬데미지감소", displayOtherLanguage = "CriDmgDown"),
    CRITDMGDOWNPER(displayName = "크리티컬데미지감소%", displayOtherLanguage = "CriDmgDown%"),
    MISS(displayName = "회피", displayOtherLanguage = "Avoidance"),
    CRITRESISTPER(displayName = "크리티컬저항%", displayOtherLanguage = "CritResist%"),

    SPEED(displayName = "속도", displayOtherLanguage = "Speed"),
    MINDAMAGE(displayName = "최소데미지", displayOtherLanguage = "MinDamage"),
    MAXDAMAGE(displayName = "최대데미지", displayOtherLanguage = "MaxDamage"),
}