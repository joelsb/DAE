<template>
    <div class="container">
        <nuxt-link :to="'/students/' + student.username + '/enroll'">Enroll</nuxt-link>
        <p></p>
        <nuxt-link :to="'/students/' + student.username + '/unroll'">Unroll</nuxt-link>
        <div v-if="student" class="student-details">
            <h2>Details of {{ username }}</h2>
            <p><strong>Username:</strong> {{ student.username }}</p>
            <p><strong>Name:</strong> {{ student.name }}</p>
            <p><strong>Email:</strong> {{ student.email }}</p>
            <!-- Add more student details as needed -->
        </div>
        <div v-if="subjects">
            <h2>Enrolled Subjects:</h2>
            <table class="subjects-table">
                <thead>
                    <tr>
                        <th>Code</th>
                        <th>Name</th>
                        <th>Course Code</th>
                        <th>Course Name</th>
                        <th>Course Year</th>
                        <th>Scholar Year</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="subject in subjects" :key="subject.code">
                        <td class="center">{{ subject.code }}</td>
                        <td class="center">{{ subject.name }}</td>
                        <td class="center">{{ subject.courseCode }}</td>
                        <td class="center">{{ subject.courseName }}</td>
                        <td class="center">{{ subject.courseYear }}</td>
                        <td class="center">{{ subject.scholarYear }}</td>
                    </tr>

                </tbody>
            </table>
        </div>
        <div v-if="messages.length > 0" class="error-messages">
            <h2>Error Messages:</h2>
            <ul>
                <li v-for="message in messages" :key="message">{{ message }}</li>
            </ul>
        </div>
    </div>
</template>
  
 
<script setup>
import { useRoute } from 'vue-router';

const route = useRoute()
const username = route.params.username
const config = useRuntimeConfig()
const api = config.public.API_URL

const { data: student, error: studentErr } = await useFetch(`${api}/students/${username}`)
const { data: subjects, error: subjectsErr } = await useFetch(`${api}/students/${username}/subjects`)
const messages = ref([])

if (studentErr.value) messages.value.push(studentErr.value)
if (subjectsErr.value) messages.value.push(subjectsErr.value)
</script>
  
<style scoped>
.container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
}

.student-details {
    margin-bottom: 20px;
}

.subjects-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
}

.subjects-table th,
.subjects-table td {
    border: 1px solid #ddd;
    padding: 10px;
    text-align: center;
}

.center {
    text-align: center;
}

.error-messages {
    color: #ff0000;
}

.error-messages ul {
    list-style: none;
    padding: 0;
}
</style>
 
  