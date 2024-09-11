import React from 'react';
import { useForm } from 'react-hook-form';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './RegisterPage.css'; // Assuming this CSS file is present

const RegisterPage = () => {
    const { register, handleSubmit, formState: { errors } } = useForm();
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
        <div className="register-container">
            <h1>Register</h1>
            <form onSubmit={handleSubmit(onSubmit)} className="register-form">
                <div className="form-group">
                    <label>Name</label>
                    <input {...register('name', { required: 'Name is required' })} placeholder="Name" />
                    {errors.name && <p className="error-text">{errors.name.message}</p>}
                </div>

                <div className="form-group">
                    <label>Last Name</label>
                    <input {...register('lastName', { required: 'Last Name is required' })} placeholder="Last Name" />
                    {errors.lastName && <p className="error-text">{errors.lastName.message}</p>}
                </div>

                <div className="form-group">
                    <label>Nickname</label>
                    <input {...register('nickName', { required: 'Nickname is required' })} placeholder="Nickname" />
                    {errors.nickName && <p className="error-text">{errors.nickName.message}</p>}
                </div>

                <div className="form-group">
                    <label>Email</label>
                    <input {...register('email', { required: 'Email is required' })} placeholder="Email" type="email" />
                    {errors.email && <p className="error-text">{errors.email.message}</p>}
                </div>

                <div className="form-group">
                    <label>Password</label>
                    <input {...register('password', { required: 'Password is required' })} type="password" placeholder="Password" />
                    {errors.password && <p className="error-text">{errors.password.message}</p>}
                </div>

                <button type="submit" className="register-button">Register</button>
            </form>
        </div>
    );
};

export default RegisterPage;
