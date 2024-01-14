<template>
    <div v-if="encomenda">
        <h2 v-once>Editar encomenda #{{ encomenda.id }}</h2>

        <form @submit.prevent="updateencomenda">
            <label for="estado">Estado: </label>
            <!-- select -->
            <select id="estado" v-model="encomendaForm.estado">
                <option value="pendente">Pendente</option>
                <option value="processamento">Processamento</option>
                <option value="transporte">Transporte</option>
                <option value="entregue">Entregue</option>
            </select>

            <span v-if="encomendaForm.estado !== null && !isEstadoValid" class="error">
                ERRO: {{ formFeedback.estado }}</span>
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
const route = useRoute();
const id = route.params.id;
const config = useRuntimeConfig();
const api = config.public.API_URL;

const encomenda = ref(null);
const messages = ref([]);

const encomendaForm = reactive({
    estado: null
});

const formFeedback = reactive({
    estado: ""
});

const fetcheEncomendas = async () => {
    try {
        const response = await fetch(`${api}/encomendas/${id}`);
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        encomenda.value = await response.json();
        encomendaForm.estado = encomenda.value.estado;
    } catch (error) {
        messages.value.push(error.message);
    }
};

const estadosPermitidos = ["pendente", "processamento", "transporte", "entregue"];

const isEstadoValid = computed(() => {
    if (!estadosPermitidos.includes(encomendaForm.estado)) {
        formFeedback.estado = "O estado deve ser pendente, processamento, transporte ou entregue";
        return false;
    }
    formFeedback.estado = "";
    return true;
});

const isFormValid = computed(() => {
    return isEstadoValid.value;
});

const updateencomenda = async () => {
    try {
        const requestOptions = {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(encomendaForm),
        };

        const response = await fetch(`${api}/encomendas/${id}/estado`, requestOptions);
        if (!response.ok) {
            throw new Error(response.statusText);
        }
        navigateTo("/encomendas");
    } catch (error) {
        messages.value.push(error.message);
    }
};

fetcheEncomendas();
</script>
  