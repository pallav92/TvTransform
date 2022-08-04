package com.tvolution.tvtransformer

import java.sql.Timestamp

/*          momentUrl: "https://images.unsplash.com/photo-1529248236319-f679e0ba552d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1344&q=80"
            source: "HBO"
            timestamp: "1659542294705"
            title: "Game Of Thrones"
            category: 'thriller'*/
data class Moment(val momentUrl:String, val source:String, val timestamp: String, val title:String, val category: String)
