<script setup>
import Navbar from "~/layouts/nav-bar.vue";
</script>

<template>
    <Navbar/>
    <h1 class="display-4 mb-4">Welcome to Intellipac</h1>
</template>

<script setup lang="ts"></script>

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
    color: #fff;
    margin: 20px;
}

.content-container {
    padding: 20px;
}
</style>
