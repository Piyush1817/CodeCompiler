import './App.css';

import { useState, useEffect } from 'react';
import { executeCode } from "./services/api";
import { templates } from "./services/templates";
import LanguageSelector from './components/LanguageSelector';
import RunButton from './components/RunButton';
import CodeEditor from "./components/CodeEditor";
import InputBox from "./components/InputBox";
import OutputBox from "./components/OutputBox";
import ClearButton from './components/ClearButton';
import DownloadButton from "./components/DownloadButton";

function App() {

    const [loading, setLoading] =
        useState(false);

    const [language, setLanguage] =
        useState("java");

    const [code, setCode] =
        useState(templates.java);

    const [input, setInput] =
        useState("");

    const [output, setOutput] =
        useState("");

    const [status, setStatus] =
        useState("");

    const [executionTime, setExecutionTime] =
        useState(0);

    async function handleRun() {

        if (!code.trim()) {

            setStatus("ERROR");
            setOutput("Please enter code.");

            return;
        }

        try {

            setLoading(true);

            setOutput("");
            setStatus("");
            setExecutionTime(0);

            const result =
                await executeCode(
                    language,
                    code,
                    input
                );

            setStatus(
                result.status
            );

            setExecutionTime(
                result.executionTime
            );

            if (result.status === "SUCCESS") {

                setOutput(
                    result.output
                );

            } else {

                setOutput(
                    result.error
                );

            }

        } catch (error) {

            setStatus("ERROR");

            setOutput(
                error?.message ||
                "Backend connection failed."
            );

        } finally {

            setLoading(false);

        }
    }

    function handleClear() {

        setOutput("");
        setInput("");
        setStatus("");
        setExecutionTime(0);

    }

    useEffect(() => {

        function handleKeyDown(event) {

            if (
                event.ctrlKey &&
                event.key === "Enter"
            ) {

                handleRun();
            }
        }

        window.addEventListener(
            "keydown",
            handleKeyDown
        );

        return () =>
            window.removeEventListener(
                "keydown",
                handleKeyDown
            );

    }, [language, code, input]);

    return (

        <div className="app-container">
                <div className="hero-section">

                                    <h1 className="hero-title">
                                   Code Compiler
                                  </h1>

                        <p className="hero-subtitle">
                           Compile and execute Java, Python and C++ code instantly
                            </p>

                    </div>

            <div className="top-bar">

                <LanguageSelector
                    language={language}
                    setLanguage={(newLanguage) => {

                        setLanguage(
                            newLanguage
                        );

                        setCode(
                            templates[newLanguage]
                        );

                    }}
                />

                <RunButton
                    onRun={handleRun}
                    loading={loading}
                />

                <ClearButton
                    onClear={handleClear}
                />
                <DownloadButton
                              code={code}
                              language={language}
                         />

            </div>

            <div className="main-layout">

                <div className="editor-panel panel">

                    <h3>Code Editor</h3>

                    <div
                        style={{
                            flex: 1
                        }}
                    >
                        <CodeEditor
                            language={language}
                            code={code}
                            setCode={setCode}
                            onRun={handleRun}
                        />
                    </div>

                </div>

                <div className="right-panel">

                    <div className="panel input-panel">

                        <h3>Input</h3>

                        <InputBox
                            input={input}
                            setInput={setInput}
                        />

                    </div>

                    <div className="panel output-panel">

                        <h3>Output</h3>

                        <OutputBox
                            output={output}
                            status={status}
                            executionTime={executionTime}
                        />

                    </div>

                </div>

            </div>

            <footer
                style={{
                    textAlign: "center",
                    marginTop: "20px",
                    color: "#888"
                }}
            >
                Built with React + Spring Boot
            </footer>

        </div>

    );
}

export default App; 