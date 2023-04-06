import logo from './logo.svg';
import './App.css';
import { useState } from 'react';

function App() {
  const [error, setError] = useState("");
  const [file, setFile] = useState("");

  const handleInputFileChange = (e) => {
    setFile(e.target.files[0])
  }

  const showError = (error) => {
    setError(error)

    setTimeout(() => {
      setError("")
    }, 2000)
  }

  const handleSubmit = (e) => {
    e.preventDefault();

    if(!file.name.match(/\.(xls|xlsx)$/)) return showError("Precisa ser um arquivo excell");

    

  }

  return (
    <>
      <form onSubmit={handleSubmit}>
        <label>
          Cliente
          <input type="text" />
        </label>
        <label>
          Arquivo excell
          <input type="file" onChange={handleInputFileChange} />
        </label>
        <input type="submit" />
      </form>
      {error && <p>{error}</p>}
    </>
  );
}

export default App;
