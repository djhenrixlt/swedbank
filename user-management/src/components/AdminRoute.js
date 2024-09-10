import React from 'react';
import { Navigate } from 'react-router-dom';

const AdminRoute = ({ component: Component }) => {
    const user = JSON.parse(localStorage.getItem('user'));

    return user && user.roles.includes('ROLE_ADMIN') ? <Component /> : <Navigate to="/login" />;
};

export default AdminRoute;

