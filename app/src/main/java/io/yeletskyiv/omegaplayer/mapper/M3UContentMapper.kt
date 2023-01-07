package io.yeletskyiv.omegaplayer.mapper

import io.yeletskyiv.omegaplayer.model.entity.M3UVideoItem
import io.yeletskyiv.omegaplayer.model.entity.VideoCategory
import io.yeletskyiv.omegaplayer.model.relations.CategoryWithVideos

private const val EXT_M3U = "#EXTM3U"
private const val EXT_INF = "#EXTINF"
private const val EXT_ID = "tvg-id"
private const val EXT_NAME = "tvg-name"
private const val EXT_TYPE = "tvg-type"
private const val EXT_GROUP = "group-title"
private const val EXT_LOGO = "tvg-logo"

fun mapM3ULinkContent(data: String): List<CategoryWithVideos> {
    if (data.contains(EXT_M3U).not()) return emptyList()

    val groups = mutableSetOf<String>()
    val itemList = mutableListOf<Pair<String, M3UVideoItem>>()
    data.split(EXT_INF).let { it.subList(1, it.size) }.forEachIndexed { index, item ->
        val group = parseExt(item, EXT_GROUP).orEmpty()
        groups.add(group)

        val id = parseExt(item, EXT_ID).orEmpty()
        val videoItem = M3UVideoItem(
            id = id.ifBlank { index.toString() },
            videoCategoryId = groups.indexOf(group),
            title = parseName(item, id).orEmpty(),
            imageUrl = parseExt(item, EXT_LOGO),
            mediaUrl = parseUrl(item),
        )

        itemList.add(Pair(group, videoItem))
    }

    val categoryList = mutableListOf<CategoryWithVideos>()
    groups.forEachIndexed { index, title ->
        categoryList.add(
            CategoryWithVideos(
                category = VideoCategory(
                    categoryId =index,
                    name = title,
                ),
                videos = itemList.filter { it.first == title }.map { it.second }
            )
        )
    }

    return categoryList
}

private fun parseExt(data: String, ext: String) = data
    .split(ext)
    .getOrNull(1)
    ?.split("\"")
    ?.getOrNull(1)

private fun parseUrl(data: String) = data
    .split("\n")
    .getOrNull(1)

private fun parseName(data: String, id: String) = data
    .split("\n")
    .firstOrNull()
    ?.split(Regex("=\"(.*)\".?,"))
    ?.last()
    ?.apply { if (id.isNotBlank()) split(id).firstOrNull()?.trim('(', ' ') }