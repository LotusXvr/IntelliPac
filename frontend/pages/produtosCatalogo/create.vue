<template>
    <form @submit.prevent="create">
        <Navbar />

        <label for="username">Nome: </label>
        <input id="username" v-model="produtoForm.nome" />
        <span class="error" v-if="!isNameValid"> ERRO: {{ formFeedback.nome }} </span>
        <br />
        <label for="peso">Peso: </label>
        <input id="peso" v-model="produtoForm.peso" />
        <span class="error" v-if="!isPesoValid">ERRO: {{ formFeedback.peso }}</span>
        <br />
        <h5>Embalagens a criar:</h5>
        Primárias:
        <span v-for="embalagem in tiposEmbalagemPrimaria">
            <br />
            <input type="checkbox" :value="embalagem.id" v-model="produtoForm.embalagensACriar" :disabled="isTipoEmbalagemPrimariaSelected &&
                !produtoForm.embalagensACriar.includes(embalagem.id)
                " />
            {{ embalagem.material }} -
            {{ construirTamanhoString(embalagem.comprimento, embalagem.largura, embalagem.altura) }}
        </span>

        <br />
        <span class="error" v-if="!isEmbalagemSelected">
            ERRO: {{ formFeedback.embalagensACriar }}
        </span>
        <br />
        Secundárias:
        <span v-for="embalagem in tiposEmbalagemSecundaria">
            <br />
            <input type="checkbox" :value="embalagem.id" v-model="produtoForm.embalagensACriar" :disabled="isTipoEmbalagemSecundariaSelected &&
                !produtoForm.embalagensACriar.includes(embalagem.id)
                " />
            {{ embalagem.material }} -
            {{ construirTamanhoString(embalagem.comprimento, embalagem.largura, embalagem.altura) }}
        </span>
        <br />
        <br />
        Terciárias:
        <span v-for="embalagem in tiposEmbalagemTercearia">
            <br />
            <input type="checkbox" :value="embalagem.id" v-model="produtoForm.embalagensACriar" :disabled="isTipoEmbalagemTerceariaSelected &&
                !produtoForm.embalagensACriar.includes(embalagem.id)
                " />
            {{ embalagem.material }} -
            {{ construirTamanhoString(embalagem.comprimento, embalagem.largura, embalagem.altura) }}
        </span>

        <br />
        <br />
        <button type="submit" :disabled="!isFormValid">Criar produto</button>
    </form>
    {{ message }}
</template>
<style scoped>
.error {
    color: red;
}
</style>
<script setup>
import Navbar from "~/layouts/nav-bar.vue"
import { ref, reactive, computed } from "vue"
import { useAuthStore } from "../store/auth-store.js"

const authStore = useAuthStore()
const { user } = authStore

const produtoForm = reactive({
    nome: null,
    fabricanteUsername: user.username,
    peso: null,
    embalagensACriar: [],
})

const formFeedback = reactive({
    nome: "",
    fabricanteUsername: "",
    peso: "",
    embalagensACriar: "",
})

const config = useRuntimeConfig()
const api = config.public.API_URL
const message = ref("")

const { data: embalagensACriar } = await useFetch(`${api}/tipoEmbalagens`, { method: "GET", headers: {'Authorization': 'Bearer ' + authStore.token}})

const tiposEmbalagemPrimaria = computed(() => {
    return embalagensACriar.value.filter((embalagem) => embalagem.tipo == 1)
})

const tiposEmbalagemSecundaria = computed(() => {
    return embalagensACriar.value.filter((embalagem) => embalagem.tipo == 2)
})

const tiposEmbalagemTercearia = computed(() => {
    return embalagensACriar.value.filter((embalagem) => embalagem.tipo == 3)
})

const isTipoEmbalagemPrimariaSelected = computed(() => {
    return produtoForm.embalagensACriar.some((id) => {
        const embalagem = embalagensACriar.value.find((embalagem) => embalagem.id == id)
        return embalagem.tipo == 1
    })
})

const isTipoEmbalagemSecundariaSelected = computed(() => {
    return produtoForm.embalagensACriar.some((id) => {
        const embalagem = embalagensACriar.value.find((embalagem) => embalagem.id == id)
        return embalagem.tipo == 2
    })
})

const isTipoEmbalagemTerceariaSelected = computed(() => {
    return produtoForm.embalagensACriar.some((id) => {
        const embalagem = embalagensACriar.value.find((embalagem) => embalagem.id == id)
        return embalagem.tipo == 3
    })
})

const isNameValid = computed(() => {
    if (produtoForm.nome === null) {
        formFeedback.nome = "O nome não pode ser vazio"
        return false
    }
    if (produtoForm.nome.length < 3) {
        formFeedback.nome = "O nome deve ter pelo menos 3 caracteres"
        return false
    }
    if (produtoForm.nome.length > 20) {
        formFeedback.nome = "O nome deve ter no máximo 20 caracteres"
        return false
    }
    formFeedback.nome = ""
    return true
})

const isPesoValid = computed(() => {
    if (produtoForm.peso !== null && !isNaN(produtoForm.peso) && produtoForm.peso > 0) {
        formFeedback.peso = ""
        return true
    }
    formFeedback.peso = "O peso tem de ser maior que 0"
    return false
})

const isEmbalagemSelected = computed(() => {
    console.log(isTipoEmbalagemPrimariaSelected.value)
    if (!isTipoEmbalagemPrimariaSelected.value) {
        formFeedback.embalagensACriar = "Escolha pelo menos uma embalagem primária"
        return false
    }
    formFeedback.embalagensACriar = ""
    return true
})

const isFormValid = computed(() => {
    return isNameValid.value && isPesoValid.value && produtoForm.embalagensACriar.length > 0 && isEmbalagemSelected.value
})

const tipoNumeroParaString = (tipo) => {
    switch (tipo) {
        case 1:
            return "Primária"
        case 2:
            return "Secundária"
        case 3:
            return "Terceária"
        default:
            return "Tipo desconhecido"
    }
}

const construirTamanhoString = (comprimento, largura, altura) => {
    return `${comprimento}x${largura}x${altura}`
}

async function create() {
    const requestBody = {
        nome: produtoForm.nome,
        fabricanteUsername: produtoForm.fabricanteUsername,
        peso: produtoForm.peso,
        embalagensACriar: produtoForm.embalagensACriar.map((id) => ({ id })),
    }

    const requestOptions = {
        method: "POST",
        headers: { 
            "Content-Type": "application/json",
            'Authorization': 'Bearer ' + authStore.token
        },
        body: JSON.stringify(requestBody),
    }

    const { error } = await useFetch(`${api}/produtosCatalogo`, requestOptions)
    if (!error.value) navigateTo("/produtosCatalogo")
    message.value = error.value
}
</script>
