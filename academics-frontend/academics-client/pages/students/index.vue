<template>
    <div>
      <div v-if="error" class="error-message">
        Error: {{ error.message }} 
      </div>
      <div v-else>
        <nuxt-link to="/create">Create a New Student</nuxt-link>
        <h2>Students</h2>
        <table class="students-table">
          <thead>
            <tr>
              <th>Username</th>
              <th>Name</th>
              <th>E-mail</th>
              <th>Course</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="student in students" :key="student.username">
              <td class="center">{{ student.username }}</td>
              <td class="center">{{ student.name }}</td>
              <td class="center">{{ student.email }}</td>
              <td class="center">{{ student.courseName }}</td>
              <td class="center">
                <nuxt-link :to="`/students/${student.username}`">Details</nuxt-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <button @click.prevent="refresh" class="refresh-button">Refresh Data</button>
    </div>
  </template>
  
  <script setup>
  const config = useRuntimeConfig()
  const api = config.public.API_URL
  const { data: students, error, refresh } = await useFetch(`${api}/students`)
  </script>
  
  <style scoped>
  .students-table {
    width: 100%;
    border-collapse: collapse;
    margin-top: 20px;
  }
  
  .students-table th,
  .students-table td {
    border: 1px solid #ddd;
    padding: 10px;
    text-align: center;
  }
  
  .center {
    text-align: center;
  }
  
  .error-message {
    color: #ff0000;
    margin-bottom: 20px;
  }
  
  .refresh-button {
    margin-top: 20px;
  }
  </style>
  