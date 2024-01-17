<template>
    <Navbar />

<p> HEYYY </p>

</template>

<script setup>
import Navbar from "~/layouts/nav-bar.vue"
import { useAuthStore } from "~/store/auth-store"
const authStore = useAuthStore()
const { user } = authStore
const route = useRoute()
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL


</script>