import Editor from "@monaco-editor/react";

function CodeEditor(props) {

    return (
        <Editor
            key={props.language}
            height="100%"
            language={props.language}
            value={props.code}
            onChange={props.setCode}
            theme="vs-dark"
            onMount={(editor, monaco) => {

        editor.addCommand(
            monaco.KeyMod.CtrlCmd |
            monaco.KeyCode.Enter,
            () => {
                props.onRun();
            }
        );

    }}
        />
    );

}

export default CodeEditor;