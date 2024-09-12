import React, { useState, useEffect } from 'react';
import axios from 'axios';

const CurrencyExchangePage = () => {
    const [rates, setRates] = useState({});
    const [amount, setAmount] = useState(0);
    const [fromCurrency, setFromCurrency] = useState('USD');
    const [toCurrency, setToCurrency] = useState('EUR');
    const [result, setResult] = useState(null);

    useEffect(() => {
        // Fetch exchange rates from an external API or your backend
        const fetchRates = async () => {
            try {
                const response = await axios.get('https://api.exchangerate-api.com/v4/latest/USD');
                setRates(response.data.rates);
            } catch (error) {
                console.error("Error fetching exchange rates:", error);
            }
        };

        fetchRates();
    }, []);

    const handleExchange = () => {
        const rate = rates[toCurrency] / rates[fromCurrency];
        const convertedAmount = (amount * rate).toFixed(2);
        setResult(`${amount} ${fromCurrency} = ${convertedAmount} ${toCurrency}`);
    };

    return (
        <div className="currency-exchange-container">
            <h1>Currency Exchange</h1>
            <div className="exchange-form">
                <input
                    type="number"
                    value={amount}
                    onChange={(e) => setAmount(e.target.value)}
                    placeholder="Amount"
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
            {result && <p>{result}</p>}
        </div>
    );
};

export default CurrencyExchangePage;
