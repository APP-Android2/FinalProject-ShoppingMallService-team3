package kr.co.lion.farming_customer.model

data class CommunityPostModel(val postIdx : Int = 0,
                              val postUserIdx	: Int = 0,
                              val postType : String = "",
                              val postTitle : String = "",
                              val postContent	: String = "",
                              val postLikeCnt	: Int = 0,
                              val postCommentCnt	: Int = 0,
                              val postViewCnt	: Int = 0,
                              val postRegDt	: String = "",
                              val postModDt : String = "",
                              val postImages	: List<String> = emptyList(),
                              val postStatus : Int = 0)
