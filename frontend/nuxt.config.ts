// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
    devtools: { enabled: true },
    runtimeConfig: {
        public: {
            API_URL: process.env.API_URL || "http://localhost:8080/intellipac/api",
        },
    },
    modules: [
        // . . .
        "@pinia/nuxt",
    ],
    plugins: [{ src: "~/plugins/chart.js", mode: "client" }],
})
