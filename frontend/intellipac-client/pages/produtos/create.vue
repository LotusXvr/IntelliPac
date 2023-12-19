<template>
  <form @submit.prevent="create">
    <label for="username">Nome</label>
    <input id="username" v-model="produtoForm.nome" />
    <span class="error">{{ formFeedback.nome }}</span>
    <br>
    <div>Fabricante:
      <select v-model="produtoForm.fabricanteID">
        <option value="">--- Please select Fabricante ---</option>
        <option v-for="fabricante in fabricantes" :value="fabricante.id">
          {{ fabricante.nomeFabricante }}
        </option>
      </select>
      <span v-if="produtoForm.idFabricante !== null && !isFabricanteValid" class="error">
 ERROR: {{ formFeedback.idFabricante }}</span></div>

    <br />
    <button type="submit" :disabled="!isFormValid">Create Product</button>
  </form>
  {{ message }}
</template>
<style scoped>
.error {
  color: red;
}
</style>
<script setup>
import { ref, reactive, computed } from 'vue'
const produtoForm = reactive({
  nome: null,
  idFabricante: null // Alterado de Number para aceitar nulos
})


const formFeedback = reactive({
  nome: '',
  idFabricante: ''
})

const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: fabricantes } = await useFetch(`${api}/fabricantes`)
const message = ref('')

const isNameValid = computed(() => {
  if (produtoForm.nome === null) {
    return false
  }
  if (produtoForm.nome.length < 3) {
    formFeedback.nome = 'Name must be at least 3 characters long'
    return false
  }
  if (produtoForm.nome.length > 20) {
    formFeedback.nome = 'Name must be at most 20 characters long'
    return false
  }
  formFeedback.nome = ''
  return true
})

const isFabricanteValid = computed(() => {
  if (produtoForm.fabricanteID === null) {
    return false
  }

  formFeedback.fabricanteID = ''
  return true
})

const isFormValid = computed(() => {
  return isNameValid.value && isFabricanteValid.value
})

async function create() {
  // Converte fabricanteID para número
  produtoForm.idFabricante = Number(produtoForm.fabricanteID);

  const requestOptions = {
    method: 'POST',
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(produtoForm)
  }

  const { error } = await useFetch(`${api}/produtos`, requestOptions)
  if (!error.value)
    navigateTo('/')
  message.value = error.value
}



</script>