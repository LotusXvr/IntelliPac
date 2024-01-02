<template>
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="sensores/create">Criar novo Sensor</nuxt-link>
        <h2>Sensores</h2>
        <table>
            <tr>
                <th>id sensor</th>
                <th>Tipo</th>
                <th>Unidade</th>
                <th>Valor</th>
                <th>Ações</th>
            </tr>
            <tr v-for="sensor in sensores">
                <td>{{ sensor.idSensor }}</td>
                <td>{{ sensor.tipo }}</td>
                <td>{{ sensor.unidade }}</td>
                <td>{{ sensor.valor }}</td>
                <td>
                    <nuxt-link :to="`/sensores/${sensor.idSensor}`">Detalhes</nuxt-link>
                    |
                    <nuxt-link :to="'/sensores/edit/' + sensor.idSensor">Editar</nuxt-link>
                    |
                    <button @click="deleteProduto(sensor.idSensor)">Excluir</button>
                </td>
            </tr>
        </table>
    </div>
    <br />
    <button @click.prevent="refresh">Refresh Data</button> <br />
    <br />
    <nuxt-link to="/">Voltar à Home</nuxt-link>
</template>
<script setup>
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: sensores, error, refresh } = await useFetch(`${api}/sensores`)

const deleteProduto = async (id) => {
    try {
        const response = await fetch(`${api}/sensores/${id}`, {
            method: "DELETE",
        })
        if (response.ok) {
            refresh()
        }
    } catch (error) {
        console.error(error)
    }
}
</script>
