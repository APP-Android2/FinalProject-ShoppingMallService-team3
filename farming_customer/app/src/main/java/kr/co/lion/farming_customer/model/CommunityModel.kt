package kr.co.lion.farming_customer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CommunityModel(var postIdx:Int, var postUserIdx:Int, var postType:String, var postTitle:String,
                          var postContent:String, var postLikeCnt:Int, var postCommentCnt:Int, var postViewCnt:Int,
                          var postRegDt:String, var postModDt:String, var postImages:MutableList<String>?, var postStatus:Int) : Parcelable {
    constructor():this(0, 0, "","", "", 0, 0, 0, "", "", mutableListOf(), 0)
}