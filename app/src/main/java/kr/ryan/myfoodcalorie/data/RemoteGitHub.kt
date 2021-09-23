package kr.ryan.myfoodcalorie.data

import com.google.gson.annotations.SerializedName

/**
 * MyFoodCalorie
 * Class: RemoteGitHub
 * Created by pyg10.
 * Created On 2021-09-23.
 * Description:
 */
data class RemoteGitHub(
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: String,
    @SerializedName("created_at") val date: String,
    @SerializedName("html_url") val url: String
)
