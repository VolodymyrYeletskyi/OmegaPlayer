package io.yeletskyiv.omegaplayer.ui.home.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import io.yeletskyiv.omegaplayer.model.relations.CategoryWithVideos

class CategoryWithVideosDiffUtil : DiffUtil.ItemCallback<CategoryWithVideos>() {

    override fun areItemsTheSame(
        oldItem: CategoryWithVideos,
        newItem: CategoryWithVideos
    ): Boolean {
        return oldItem.category.categoryId == newItem.category.categoryId
    }

    override fun areContentsTheSame(
        oldItem: CategoryWithVideos,
        newItem: CategoryWithVideos
    ): Boolean {
        return oldItem.category.categoryId == newItem.category.categoryId &&
                oldItem.category.name == newItem.category.name
    }
}