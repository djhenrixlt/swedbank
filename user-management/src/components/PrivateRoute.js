import React from 'react';
import { Navigate } from 'react-router-dom';

const PrivateRoute = ({ component: Component }) => {
    const user = JSON.parse(localStorage.getItem('user'));

    return user ? <Component /> : <Navigate to="/login" />;
};

export default PrivateRoute;

