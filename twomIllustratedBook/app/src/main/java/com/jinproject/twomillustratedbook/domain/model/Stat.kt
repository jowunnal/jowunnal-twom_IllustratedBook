package com.jinproject.twomillustratedbook.domain.model

enum class Stat(val displayName: String) {
    Hp(displayName = "체력"),
    Mp(displayName = "마나"),
    HpPer(displayName = "체력%"),
    MpPer(displayName = "마나%"),
    HpRegen(displayName = "체력재생"),
    MpRegen(displayName = "마나재생"),
    Hr(displayName = "명중"),
    Cri(displayName = "크리티컬"),
    StatInt(displayName = "지능"),
    StatStr(displayName = "힘"),
    StatDex(displayName = "덱스"),
    Move(displayName = "이동속도"),
    Armor(displayName = "방어력"),
    PveDmg(displayName = "pve데미지증가"),
    PvpDmg(displayName = "pvp데미지증가"),
    PveDmgPer(displayName = "pve데미지증가%"),
    PvpDmgPer(displayName = "pvp데미지증가%"),
    PveDmgDown(displayName = "pve데미지감소"),
    PvpDmgDown(displayName = "pvp데미지감소"),
    PveDmgDownPer(displayName = "pve데미지감소%"),
    PvpDmgDownPer(displayName = "pvp데미지감소%"),
    GoldDrop(displayName = "골드드랍률"),
    ItemDrop(displayName = "아이템드랍률"),
    BossDmgPer(displayName = "보스데미지%"),
    CriDmgDown(displayName = "크리티컬데미지감소"),
    CriDmgDownPer(displayName = "크리티컬데미지감소%"),
    Miss(displayName = "회피"),
    CriResistPer(displayName = "크리티컬저항%")
}