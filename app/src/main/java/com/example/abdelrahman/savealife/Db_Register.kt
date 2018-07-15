package com.example.abdelrahman.savealife

/**
 * Created by Abdelrahman on 3/13/2018.
 */
class Db_Register (var id:String,var firstname:String,var lastname:String,var Email:String
                   ,var Birthday:String,var age:String,var phone:String,var photoUrl:String
                   ,var gender:String,var bloodGroub:String,var lastdonner:String
                    ,var longtude:String,val latetude:String){
constructor():this("","","","","","","","","","","","",""){

}
    fun get_Db_Register_id():String{
        return id
    }
}