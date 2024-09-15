import React from 'react';
import { useNavigate } from 'react-router-dom';

const DashboardPage: React.FC = () => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('jwtToken'); // Remove the JWT token
        navigate('/login');
    };

    const navigateToCurrencyExchange = () => {
        navigate('/currency-exchange');
    };

    return (
        <div className="dashboard-container">
            <h1>Welcome to your Dashboard</h1>
            <p>Congratulations on logging in! You can now access additional features.</p>
            <button onClick={navigateToCurrencyExchange} className="dashboard-btn">Currency Exchange</button>
            <button onClick={handleLogout} className="dashboard-btn">Logout</button>
        </div>
    );
};

export default DashboardPage;
