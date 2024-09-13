import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './CurrencyExchangePage.css'; // Import the CSS file

const CurrencyExchangePage = () => {
    const [rates, setRates] = useState({});
    const [amount, setAmount] = useState(0);
    const [fromCurrency, setFromCurrency] = useState('USD');
    const [toCurrency, setToCurrency] = useState('EUR');
    const [result, setResult] = useState(null);
    const [loading, setLoading] = useState(true); // Add loading state
    const [error, setError] = useState(null); // Add error state

    useEffect(() => {
        // Fetch exchange rates from an external API
        const fetchRates = async () => {
            try {
                const response = await axios.get('https://api.exchangerate-api.com/v4/latest/USD');
                setRates(response.data.rates);
                setLoading(false); // Set loading to false after fetching data
            } catch (error) {
                setError("Error fetching exchange rates.");
                setLoading(false); // Set loading to false on error
            }
        };

        fetchRates();
    }, []);

    const handleExchange = () => {
        if (!rates[fromCurrency] || !rates[toCurrency]) {
            setError("Invalid currency selected.");
            return;
        }
        const rate = rates[toCurrency] / rates[fromCurrency];
        const convertedAmount = (amount * rate).toFixed(2);
        setResult(`${amount} ${fromCurrency} = ${convertedAmount} ${toCurrency}`);
    };

    if (loading) {
        return <div className="loading">Loading exchange rates...</div>;
    }

    if (error) {
        return <div className="error">{error}</div>;
    }

    return (
        <div className="currency-exchange-container">
            <h1>Currency Exchange</h1>
            <div className="exchange-form">
                <input
                    type="number"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                    placeholder="Amount"
                    min="0"
                />
                <select value={fromCurrency} onChange={(e) => setFromCurrency(e.target.value)}>
                    {Object.keys(rates).map(currency => (
                        <option key={currency} value={currency}>{currency}</option>
                    ))}
                </select>
                <select value={toCurrency} onChange={(e) => setToCurrency(e.target.value)}>
                    {Object.keys(rates).map(currency => (
                        <option key={currency} value={currency}>{currency}</option>
                    ))}
                </select>
                <button onClick={handleExchange}>Exchange</button>
            </div>
            {result && <p className="result">{result}</p>}
        </div>
    );
};

export default CurrencyExchangePage;
