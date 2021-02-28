package com.miladheydari.farazpardazan.client.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class  Result<T>(val data:T? = null,val message:String? = null)