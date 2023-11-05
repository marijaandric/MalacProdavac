package com.example.front.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.front.R
import com.example.front.components.GalleryComponent
import com.example.front.components.ProductImage
import com.example.front.components.TitleTextComponent
import com.example.front.components.ToggleImageButton
import com.example.front.model.ImageData
import com.example.front.ui.theme.Typography
import kotlin.random.Random

@Composable
fun ProductPage(){
    Box() {
        ProductImage(painterResource(
            id = R.drawable.jabukeproduct)
        )
        ToggleImageButton(modifier = Modifier
                .align(Alignment.TopEnd))
        Image(
            painter = painterResource(id = R.drawable.backarrow),
            contentDescription = "",
            modifier = Modifier
                .size(50.dp)
                .padding(5.dp)
                .align(Alignment.TopStart)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = 350.dp)
                .background(Color.White)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .border(1.dp, Color.Black)
        ) {
            GalleryComponent(
                images = sampleImages,
                modifier = Modifier.
                            padding(20.dp))
            Text(
                text = "Jabuke zlatni delišes",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                style = Typography.titleMedium,
                    )
        }
    }
}

@Preview
@Composable
fun pregled(){
    ProductPage()
}

val sampleImages = List(10) {
    ImageData(1,"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AIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoNXjX6kejD4x46uzo/q2H7Iru3z3noez+gwuj+584sDu6v+RHS6L7j7zxm+j+61+9Irr/i42jo/t3fGYguv/Lbz4X3b/qOy9E99fctyy6/+Bz2e97yis7o/suAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1OCEF7dEHzjzd78W3X9w+F9F979688To/tlnLovun//tn0X3j3vzY9H9Pz9yZnT/uycdEd2/8Lyh6P4fz7o2uj9i4TnR/dPn/yq6f97626L7W2c/Fd0f/s+rovsuAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1MCX1h2ffWHXQ9H5zxyYGd3/8L7l0f3brx8V3X/2rvdE94/+3j3R/etHHIzuz/nXhdH91b89K7q/7JYx0f1v/MGa6P5la9+J7q+64Kjo/vM/HYzuz/jQ9ui+CwCglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKPV/AQAA///7jYKTIQYa7wAAAABJRU5ErkJggg==")
    ImageData(1,"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AIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoNXjX6kejD4x46uzo/q2H7Iru3z3noez+gwuj+584sDu6v+RHS6L7j7zxm+j+61+9Irr/i42jo/t3fGYguv/Lbz4X3b/qOy9E99fctyy6/+Bz2e97yis7o/suAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1OCEF7dEHzjzd78W3X9w+F9F979688To/tlnLovun//tn0X3j3vzY9H9Pz9yZnT/uycdEd2/8Lyh6P4fz7o2uj9i4TnR/dPn/yq6f97626L7W2c/Fd0f/s+rovsuAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1MCX1h2ffWHXQ9H5zxyYGd3/8L7l0f3brx8V3X/2rvdE94/+3j3R/etHHIzuz/nXhdH91b89K7q/7JYx0f1v/MGa6P5la9+J7q+64Kjo/vM/HYzuz/jQ9ui+CwCglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKPV/AQAA///7jYKTIQYa7wAAAABJRU5ErkJggg==")
    ImageData(1,"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AIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoNXjX6kejD4x46uzo/q2H7Iru3z3noez+gwuj+584sDu6v+RHS6L7j7zxm+j+61+9Irr/i42jo/t3fGYguv/Lbz4X3b/qOy9E99fctyy6/+Bz2e97yis7o/suAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1OCEF7dEHzjzd78W3X9w+F9F979688To/tlnLovun//tn0X3j3vzY9H9Pz9yZnT/uycdEd2/8Lyh6P4fz7o2uj9i4TnR/dPn/yq6f97626L7W2c/Fd0f/s+rovsuAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1MCX1h2ffWHXQ9H5zxyYGd3/8L7l0f3brx8V3X/2rvdE94/+3j3R/etHHIzuz/nXhdH91b89K7q/7JYx0f1v/MGa6P5la9+J7q+64Kjo/vM/HYzuz/jQ9ui+CwCglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKPV/AQAA///7jYKTIQYa7wAAAABJRU5ErkJggg==")
    ImageData(1,"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AIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoNXjX6kejD4x46uzo/q2H7Iru3z3noez+gwuj+584sDu6v+RHS6L7j7zxm+j+61+9Irr/i42jo/t3fGYguv/Lbz4X3b/qOy9E99fctyy6/+Bz2e97yis7o/suAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1OCEF7dEHzjzd78W3X9w+F9F979688To/tlnLovun//tn0X3j3vzY9H9Pz9yZnT/uycdEd2/8Lyh6P4fz7o2uj9i4TnR/dPn/yq6f97626L7W2c/Fd0f/s+rovsuAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1MCX1h2ffWHXQ9H5zxyYGd3/8L7l0f3brx8V3X/2rvdE94/+3j3R/etHHIzuz/nXhdH91b89K7q/7JYx0f1v/MGa6P5la9+J7q+64Kjo/vM/HYzuz/jQ9ui+CwCglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKPV/AQAA///7jYKTIQYa7wAAAABJRU5ErkJggg==")
    ImageData(1,"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AIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoNXjX6kejD4x46uzo/q2H7Iru3z3noez+gwuj+584sDu6v+RHS6L7j7zxm+j+61+9Irr/i42jo/t3fGYguv/Lbz4X3b/qOy9E99fctyy6/+Bz2e97yis7o/suAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1OCEF7dEHzjzd78W3X9w+F9F979688To/tlnLovun//tn0X3j3vzY9H9Pz9yZnT/uycdEd2/8Lyh6P4fz7o2uj9i4TnR/dPn/yq6f97626L7W2c/Fd0f/s+rovsuAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1MCX1h2ffWHXQ9H5zxyYGd3/8L7l0f3brx8V3X/2rvdE94/+3j3R/etHHIzuz/nXhdH91b89K7q/7JYx0f1v/MGa6P5la9+J7q+64Kjo/vM/HYzuz/jQ9ui+CwCglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKPV/AQAA///7jYKTIQYa7wAAAABJRU5ErkJggg==")
    ImageData(1,"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AIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoNXjX6kejD4x46uzo/q2H7Iru3z3noez+gwuj+584sDu6v+RHS6L7j7zxm+j+61+9Irr/i42jo/t3fGYguv/Lbz4X3b/qOy9E99fctyy6/+Bz2e97yis7o/suAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1OCEF7dEHzjzd78W3X9w+F9F979688To/tlnLovun//tn0X3j3vzY9H9Pz9yZnT/uycdEd2/8Lyh6P4fz7o2uj9i4TnR/dPn/yq6f97626L7W2c/Fd0f/s+rovsuAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1MCX1h2ffWHXQ9H5zxyYGd3/8L7l0f3brx8V3X/2rvdE94/+3j3R/etHHIzuz/nXhdH91b89K7q/7JYx0f1v/MGa6P5la9+J7q+64Kjo/vM/HYzuz/jQ9ui+CwCglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKPV/AQAA///7jYKTIQYa7wAAAABJRU5ErkJggg==")
    ImageData(1,"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AIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoNXjX6kejD4x46uzo/q2H7Iru3z3noez+gwuj+584sDu6v+RHS6L7j7zxm+j+61+9Irr/i42jo/t3fGYguv/Lbz4X3b/qOy9E99fctyy6/+Bz2e97yis7o/suAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1OCEF7dEHzjzd78W3X9w+F9F979688To/tlnLovun//tn0X3j3vzY9H9Pz9yZnT/uycdEd2/8Lyh6P4fz7o2uj9i4TnR/dPn/yq6f97626L7W2c/Fd0f/s+rovsuAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1MCX1h2ffWHXQ9H5zxyYGd3/8L7l0f3brx8V3X/2rvdE94/+3j3R/etHHIzuz/nXhdH91b89K7q/7JYx0f1v/MGa6P5la9+J7q+64Kjo/vM/HYzuz/jQ9ui+CwCglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKPV/AQAA///7jYKTIQYa7wAAAABJRU5ErkJggg==")
    ImageData(1,"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AIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoNXjX6kejD4x46uzo/q2H7Iru3z3noez+gwuj+584sDu6v+RHS6L7j7zxm+j+61+9Irr/i42jo/t3fGYguv/Lbz4X3b/qOy9E99fctyy6/+Bz2e97yis7o/suAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1OCEF7dEHzjzd78W3X9w+F9F979688To/tlnLovun//tn0X3j3vzY9H9Pz9yZnT/uycdEd2/8Lyh6P4fz7o2uj9i4TnR/dPn/yq6f97626L7W2c/Fd0f/s+rovsuAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACg1MCX1h2ffWHXQ9H5zxyYGd3/8L7l0f3brx8V3X/2rvdE94/+3j3R/etHHIzuz/nXhdH91b89K7q/7JYx0f1v/MGa6P5la9+J7q+64Kjo/vM/HYzuz/jQ9ui+CwCglAAAlBIAgFICAFBKAABKCQBAKQEAKCUAAKUEAKCUAACUEgCAUgIAUEoAAEoJAEApAQAoJQAApQQAoJQAAJQSAIBSAgBQSgAASgkAQCkBACglAAClBACglAAAlBIAgFICAFBKAABKCQBAKQEAKPV/AQAA///7jYKTIQYa7wAAAABJRU5ErkJggg==")
}