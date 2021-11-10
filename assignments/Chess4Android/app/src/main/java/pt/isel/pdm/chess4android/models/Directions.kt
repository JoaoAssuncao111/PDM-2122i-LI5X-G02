package pt.isel.pdm.chess4android.models

enum class Directions (val row: Int, val column: Int){
    UP(1,0),
    DOWN(-1,0),
    LEFT(0,-1),
    RIGHT(0,+1),
    UP_LEFT(1,-1),
    UP_RIGHT(1,1),
    DOWN_LEFT(-1,-1),
    DOWN_RIGHT(-1,+1);

}

