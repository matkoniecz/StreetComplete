package de.westnordost.streetcomplete.quests.bike_parking_type

enum class BarrierType(val osmValue: String) {
    // /home/mateusz/Documents/install_moje/rescale-for-android
    GATE("gate"), // also fitting for swing gate: https://wiki.openstreetmap.org/wiki/File:Lovely_gates_near_Hollinshead_Swing_Bridge_-_geograph.org.uk_-_844479.jpg
    LIFT_GATE("lift_gate"), // https://wiki.openstreetmap.org/wiki/File:Lift_gate.jpg
    BOLLARD("bollard"), // https://commons.wikimedia.org/wiki/File:Stilpoller_Kugelkopf.jpg
    CHAIN("chain"), // https://commons.wikimedia.org/wiki/File:Andernach_-_Schlossstra%C3%9Fe_-_Schloss_Burg_Namedy_26_ies.jpg
    ROPE("rope"), // https://wiki.openstreetmap.org/wiki/File:Black_Red_Gold_Rope_at_German_Bundestag_in_Berlin_2010.jpg
    WIRE_GATE("hampshire_gate"),
    CATTLE_GRID("cattle_grid"),// https://wiki.openstreetmap.org/wiki/File:Cattle_grid.jpg
    BLOCK("block"), // https://commons.wikimedia.org/wiki/File:Oblast_mezi_Libe%C5%88sk%C3%BDm_mostem_a_Negrelliho_viaduktem_(06).jpg
    JERSEY_BARRIER("jersey_barrier"), // https://commons.wikimedia.org/wiki/File:BarreiraNewJersey.JPG https://commons.wikimedia.org/wiki/Category:Jersey_barriers
    LOG("log"), // https://commons.wikimedia.org/wiki/Category:Logs_across_paths https://commons.wikimedia.org/wiki/File:Sihlwald_windthrow_Spinnerweg_20200204_1.jpg
    KERB("curb"), // ????????????????????????
    HEIGHT_RESTRICTOR("height_restrictor"), // https://commons.wikimedia.org/wiki/File:Sainsbury%27s_car_park_height_restriction_barrier,_Chingford,_London,_England_1.jpg
    FULL_HEIGHT_TURNSTILE("full-height_turnstile"), // https://en.wikipedia.org/wiki/File:Type_B_Series_Roto-Gate.jpg
    TURNSTILE("turnstile"), // https://commons.wikimedia.org/wiki/File:%D0%A2%D1%83%D1%80%D0%BD%D0%B8%D0%BA%D0%B5%D1%82%D1%8B_%D0%B4%D0%BB%D1%8F_%D0%BC%D0%BE%D0%B4%D1%83%D0%BB%D1%8F_%22%D0%92%D1%85%D0%BE%D0%B4-%D0%B2%D1%8B%D1%85%D0%BE%D0%B4%22.JPG
    PASSAGE("entrance"), // https://wiki.openstreetmap.org/wiki/File:Archway_between_walled_gardens,_Greenway_-_geograph.org.uk_-_191221.jpg # https://commons.wikimedia.org/wiki/File:Doorgang_in_muur._Locatie,_Chinese_tuin_Het_Verborgen_Rijk_van_Ming._Locatie._Hortus_Haren_01.jpg?fastcci_from=69893&c1=69893&d1=15&s=200&a=fqv
    DEBRIS_PILE("debris"), //??????????? // https://wiki.openstreetmap.org/wiki/File:Barrier_debris.jpg
    STILE_SQUEEZER("stile"),// https://en.wikipedia.org/wiki/File:Squeezer_stile._-_geograph.org.uk_-_110502.jpg
    STILE_LADDER("stile"), // https://en.wikipedia.org/wiki/File:Ladder_stile_Snowdonia.jpg
    STILE_STEPOVER("stile"), // https://en.wikipedia.org/wiki/File:Stile_with_dog_gate_OS_SY567846.jpg
    KISSING_GATE("kissing_gate"),  //https://commons.wikimedia.org/wiki/File:Kissing_gate_near_Trillinghurst_Farm_-_geograph.org.uk_-_1512534.jpg https://commons.wikimedia.org/wiki/File:Kissing_Gate_above_Colvithick_Wood_-_geograph.org.uk_-_109914.jpg
    BICYCLE_BARRIER("cycle_barrier") // https://commons.wikimedia.org/wiki/File:Stade_municipal_Claude_Daragon_%C3%A0_Pecqueuse_le_6_ao%C3%BBt_2016_-_02.jpg
}
