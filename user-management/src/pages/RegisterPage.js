import React from 'react';
import { useForm } from 'react-hook-form';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const RegisterPage = () => {
    const { register, handleSubmit } = useForm();
    const navigate = useNavigate();

    const onSubmit = async (data) => {
        console.log("Submitting Data:", data); // Debugging log
        try {
            const response = await axios.post('/api/v1.0/signup', data, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            if (response.status === 201) {
                navigate('/login'); // Redirect to login page after successful registration
            }
        } catch (error) {
            if (error.response) {
                console.error("Backend responded with status", error.response.status, ":", error.response.data);
                alert(`Registration failed: ${error.response.data}`);
            } else if (error.request) {
                console.error("No response received:", error.request);
                alert('Registration failed: No response from server');
            } else {
                console.error("Error setting up request:", error.message);
                alert(`Registration failed: ${error.message}`);
            }
        }
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <input {...register('name')} placeholder="Name" required />
            <input {...register('lastName')} placeholder="Last Name" required />
            <input {...register('nickName')} placeholder="Nickname" required />
            <input {...register('email')} placeholder="Email" required />
            <input {...register('password')} type="password" placeholder="Password" required />
            <button type="submit">Register</button>
        </form>
    );
};

export default RegisterPage;

