import React, { useEffect, useState } from 'react';
import axios from 'axios';

const Analytics = ({ userId }) => {
  const [data, setData] = useState({});

  useEffect(() => {
    axios.get(`/api/v1/mock-tests/analytics/${userId}`)
      .then(res => setData(res.data));
  }, [userId]);

  return (
    <div className="mt-6 p-4 border rounded shadow bg-white">
      <h2 className="text-lg font-bold mb-2">Subject-wise Analytics</h2>
      <table className="w-full text-left">
        <thead>
          <tr>
            <th className="border p-2">Subject</th>
            <th className="border p-2">Avg Marks</th>
            <th className="border p-2">Tests Taken</th>
          </tr>
        </thead>
        <tbody>
          {Object.entries(data).map(([subject, val]) => (
            <tr key={subject}>
              <td className="border p-2">{subject}</td>
              <td className="border p-2">{val.avgMarks?.toFixed(2)}</td>
              <td className="border p-2">{val.testCount}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Analytics;
