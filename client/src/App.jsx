import { useState } from 'react'
import { Routes, Route } from 'react-router-dom';
import './App.css'
import Home from './pages/Home';
import StartTestForm from './components/mockTest/StartTestForm';
import TestRunner from './components/mockTest/TestRunner';
import TestResult from './components/mockTest/TestResult';
import Analytics from './components/mockTest/Analytics';
function App() {

  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/mock-test" element={<StartTestForm />} />
      <Route path="/test/:testId" element={<TestRunner />} />
      <Route path="/result/:testId" element={<TestResult />} />
      <Route path="/analytics" element={<Analytics />} />
      
    </Routes>
  )
}

export default App
