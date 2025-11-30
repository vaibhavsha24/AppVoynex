package com.voynex.app.data.remote

import com.voynex.app.BuildConfig

/**
 * The actual implementation of the API key, which retrieves the key from the BuildConfig.
 */
actual val ApiKey: String = BuildConfig.API_KEY
