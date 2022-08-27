package com.veit.app.weatherino

import com.veit.app.weatherino.api.Coord
import com.veit.app.weatherino.api.RequestInterceptor
import com.veit.app.weatherino.utils.LocationProvider
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RequestInterceptorTest {

    @Mock
    lateinit var locationProvider: LocationProvider

    @Mock
    lateinit var chain: Interceptor.Chain

    private lateinit var requestInterceptor: RequestInterceptor

    @Before
    fun setup() {
        requestInterceptor = RequestInterceptor(locationProvider)
    }

    @Test
    fun interceptRequest() {
        val request = Request.Builder().url("https://example.com").build()

        `when`(chain.request())
            .thenReturn(request)
        `when`(locationProvider.getLocation()).thenReturn(Coord(23.0, 24.0))

        requestInterceptor.intercept(chain)

        verify(chain).proceed(
            org.mockito.kotlin.argThat { arg ->
                arg.url.queryParameter("appid") == "7f60d1bee88dc43268cdeb629925f8aa" &&
                        arg.url.queryParameter("units") == "metric" &&
                        arg.url.queryParameter("lat") == "23.0" &&
                        arg.url.queryParameter("lon") == "24.0"
            }
        )
    }
}