package kr.co.lion.farming_customer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class CommunityModel(var postIdx:Int, var postUserIdx:Int, var postType:String, var postTitle:String, var postContent:String,
                          var postLikeCnt:Int, var postCommentCnt:Int, var postViewCnt:Int, var postLikeState:Boolean,
                          var postRegDt:String, var postModDt:String, var postImages:MutableList<String>?, var postStatus:Int) {
    constructor():this(0, 0, "","", "", 0, 0, 0, false, "", "", mutableListOf(), 0)
}