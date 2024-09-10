import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';

const UserProfilePage = () => {
    const [user, setUser] = useState(null);
    const { register, handleSubmit, reset } = useForm();
    const navigate = useNavigate();

    useEffect(() => {
        // Fetch user data from API
        const fetchUser = async () => {
            const storedUser = JSON.parse(localStorage.getItem('user'));
            try {
                const response = await axios.get(`/api/v1.0/users/${storedUser.id}`);
                setUser(response.data);
                reset(response.data);
            } catch (error) {
                alert('Failed to fetch user data');
            }
        };

        fetchUser();
    }, [reset]);

    const onSubmit = async (data) => {
        try {
            await axios.post(`/api/v1.0/users/${user.id}`, data);
            alert('Profile updated');
        } catch (error) {
            alert('Update failed');
        }
    };

    const handleDelete = async () => {
        try {
            await axios.delete(`/api/v1.0/users/${user.id}`);
            localStorage.removeItem('user');
            navigate('/login');
            alert('Account deactivated');
        } catch (error) {
            alert('Deactivation failed');
        }
    };

    if (!user) return <p>Loading...</p>;

    return (
        <div>
            <form onSubmit={handleSubmit(onSubmit)}>
                <input {...register('name')} placeholder="Name" />
                <input {...register('lastName')} placeholder="Last Name" />
                <input {...register('nickName')} placeholder="Nickname" />
                <input {...register('email')} placeholder="Email" />
                <button type="submit">Update Profile</button>
            </form>
            <button onClick={handleDelete}>Deactivate Account</button>
        </div>
    );
};

export default UserProfilePage;

