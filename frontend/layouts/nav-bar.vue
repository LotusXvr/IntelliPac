<template>
    <div>
        <div class="container d-flex flex-column align-items-center justify-content-center mt-5">
            <nav class="navbar navbar-expand-lg navbar-light bg-light">
                <div class="navbar-nav custom-center">
                    <nuxt-link to="/" class="nav-link">Home</nuxt-link>
                    <nuxt-link to="/produtosCatalogo" class="nav-link">Produtos</nuxt-link>
                    <nuxt-link to="/fabricantes" class="nav-link">Fabricantes</nuxt-link>
                    <nuxt-link to="/embalagens" class="nav-link">Embalagens</nuxt-link>
                    <nuxt-link to="/sensores" class="nav-link">Sensores</nuxt-link>
                    <nuxt-link to="/observacoes" class="nav-link">Observações</nuxt-link>
                    <nuxt-link to="/auth-test" class="nav-link">Test</nuxt-link>
                    <nuxt-link to="/auth/login" class="nav-link">Login</nuxt-link>
                    <a href="#" @click-prevent="logout" class="nav-link">Logout</a>
                </div>
            </nav>
        </div>
    </div>
    <div>
        <slot />
    </div>
</template>

<script setup lang="ts">
import { useAuthStore } from "../store/auth-store.js"
import { useRouter } from "vue-router"

const router = useRouter()
const authStore = useAuthStore()

function logout() {
    authStore.logout()
    router.push("/")
}
</script>

<style scoped>
.container {
    background-color: darkcyan;
    border-radius: 10px;
    padding: 20px;
    color: #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
    margin-bottom: 20px;
}

.nav-link {
    font: 15px "Roboto", sans-serif;
    color: #fff;
    margin: 0px 20px;
}

.custom-center {
    display: flex;
    justify-content: center;
    align-items: center;
    text-decoration: none;
}

.content-container {
    padding: 20px;
}
</style>
