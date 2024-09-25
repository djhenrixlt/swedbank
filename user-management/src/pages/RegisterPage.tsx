import React from 'react';
import { useForm, SubmitHandler } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import './RegisterPage.css';
import { registerUser, User } from './authService'; // Import registerUser from authService

const RegisterPage: React.FC = () => {
    const { register, handleSubmit, formState: { errors } } = useForm<User>();
    const navigate = useNavigate();

    // The form submit handler
    const onSubmit: SubmitHandler<User> = async (data) => {
        try {
            // Use the registerUser function to send data to the backend
            await registerUser(data);
            navigate('/login'); // Redirect to login page after successful registration
        } catch (error: any) {
            if (error.response) {
                // Handle backend errors (such as validation issues)
                console.error("Backend responded with status", error.response.status, ":", error.response.data);
                alert(`Registration failed: ${error.response.data || error.message}`);
            } else {
                // Handle unexpected errors
                console.error("Unexpected error:", error);
                alert(`Registration failed: ${error.message || 'Unknown error'}`);
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
                    <label>Username</label>
                    <input {...register('username', { required: 'Username is required' })} placeholder="Username" />
                    {errors.username && <p className="error-text">{errors.username.message}</p>}
                </div>

                <div className="form-group">
                    <label>Email</label>
                    <input {...register('email', { required: 'Email is required', pattern: { value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/, message: 'Invalid email address' } })} placeholder="Email" type="email" />
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
