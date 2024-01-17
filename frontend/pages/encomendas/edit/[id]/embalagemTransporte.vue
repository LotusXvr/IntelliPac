<template>
    <div v-if="encomenda">
        <h2 v-once>Editar encomenda #{{ encomenda.id }}</h2>

        <form @submit.prevent="updateEncomenda">
            <div>
                Embalagens de Transporte:
                <div v-for="embalagem in embalagensTransporte">
                    <br />
                    <div>
                        <input
                            type="checkbox"
                            :value="embalagem.id"
                            v-model="encomendaForm.embalagensTransporte"
                        />
                        {{ embalagem.material }} -
                        {{
                            construirTamanhoString(
                                embalagem.comprimento,
                                embalagem.largura,
                                embalagem.altura
                            )
                        }}
                        <ul v-if="embalagem.sensores.length > 0">
                            <span style="margin-left: -20px; font-weight: 600">Sensores:</span>
                            <li v-for="sensor in embalagem.sensores">
                                {{ sensor.tipo }} - {{ sensor.unidade }}
                            </li>
                        </ul>
                    </div>
                </div>
                <br />
                <span v-if="!isEmbalagemSelected" class="error">
                    ERRO: {{ formFeedback.embalagensTransporte }}</span
                >
            </div>
            <br />
            <br />
            <button type="submit" :disabled="!isFormValid">Save</button>
        </form>
        <nuxt-link to="/encomendas">Back to encomendas</nuxt-link>
    </div>
    <h2>Error messages:</h2>
    {{ messages }}
</template>
<style scoped>
.error {
    color: red;
}
</style>
<script setup>
const route = useRoute()
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL

const { data: embalagensTransporte } = await useFetch(`${api}/embalagensDeTransporte`)

const encomenda = ref(null)
const messages = ref([])
const embalagensAInserir = ref([])

const encomendaForm = reactive({
    embalagensTransporte: [],
})

const formFeedback = reactive({
    embalagensTransporte: "",
})

const construirTamanhoString = (comprimento, largura, altura) => {
    return `${comprimento}x${largura}x${altura}`
}

const fetchEncomenda = async () => {
    try {
        const response = await fetch(`${api}/encomendas/${id}`)
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        encomenda.value = await response.json()
        // Mapeie os IDs das embalagens de transporte da encomenda para encomendaForm.embalagensTransporte
        encomendaForm.embalagensTransporte = encomenda.value.embalagensTransporte.map(
            (embalagem) => embalagem.id
        )
        console.log(encomendaForm.embalagensTransporte)
    } catch (error) {
        messages.value.push(error.message)
    }
}

const isEmbalagemSelected = computed(() => {
    if (encomendaForm.embalagensTransporte.length === 0) {
        formFeedback.embalagensTransporte = "A embalagem de transporte deve ser selecionada"
        return false
    }
    formFeedback.embalagensTransporte = ""
    return true
})

const isFormValid = computed(() => {
    return isEmbalagemSelected.value
})

const updateEncomenda = async () => {
    try {
        embalagensAInserir.value = encomendaForm.embalagensTransporte.filter(
            (id) =>
                !encomenda.value.embalagensTransporte.map((embalagem) => embalagem.id).includes(id)
        )

        let requestBody = {
            embalagensTransporte: encomendaForm.embalagensTransporte.map((id) => ({ id })),
        }

        const requestOptions = {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(requestBody),
        }

        const response = await fetch(
            `${api}/encomendas/${id}/embalagensDeTransporte`,
            requestOptions
        )
        if (!response.ok) {
            throw new Error(response.statusText)
        }
        navigateTo("/encomendas")
    } catch (error) {
        messages.value.push(error.message)
    }
}

fetchEncomenda()
</script>
