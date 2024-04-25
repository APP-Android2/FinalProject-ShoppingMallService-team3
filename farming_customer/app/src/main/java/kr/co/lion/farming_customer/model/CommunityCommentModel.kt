package kr.co.lion.farming_customer.model

data class CommunityCommentModel(var commentIdx:Int, var commentUserIdx:Int, var commentPostIdx:Int, var commentContent:String,
                                 var commentCnt:Int, var commentRegDt:String,var commentModDt:String, var commentStatus:Int) {
    constructor():this(0,0,0,"", 0, "", "", 0)
}